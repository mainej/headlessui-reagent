(ns build
  (:require
   [clojure.string :as string]
   [clojure.tools.build.api :as b]
   [org.corfield.build :as bb]))

(def ^:private lib 'com.github.mainej/headlessui-reagent)
(def ^:private rev-count (Integer/parseInt (b/git-count-revs nil)))
(def ^:private headlessui-react-version "1.4.0")
(defn- format-version [revision] (str headlessui-react-version "." revision))
(def ^:private version (format-version rev-count))
(def ^:private next-version (format-version (inc rev-count)))
(def ^:private tag (str "v" version))
(def ^:private basis (b/create-basis {:root    nil
                                      :user    nil
                                      :project "deps.edn"}))

(defn- die
  ([code message & args]
   (die code (apply format message args)))
  ([code message]
   (binding [*out* *err*]
     (println message))
   (System/exit code)))

(defn- git [command args]
  (b/process (assoc args :command-args (into ["git"] command))))

(defn- git-rev []
  (let [{:keys [exit out]} (git ["rev-parse" "HEAD"] {:out :capture})]
    (when (zero? exit)
      (string/trim out))))

(defn- git-push [params]
  (when-not (and (zero? (:exit (git ["push" "origin" tag] {})))
                 (zero? (:exit (git ["push" "origin"] {}))))
    (die 15 "\nCouldn't sync with github."))
  params)

(defn- assert-changelog-updated [params]
  (when-not (string/includes? (slurp "CHANGELOG.md") tag)
    (die 10 (string/join "\n"
                         ["CHANGELOG.md must include tag."
                          "  * If you will amend the current commit, use %s"
                          "  * If you intend to create a new commit, use %s"])
         version next-version))
  params)

(defn- assert-package-json-updated [params]
  (when-not (string/includes? (slurp "package.json") version)
    (die 11 (string/join "\n"
                         ["package.json must include version."
                          "  * If you will amend the current commit, use %s"
                          "  * If you intend to create a new commit, use %s"])
         version next-version))
  params)

(defn- assert-scm-clean [params]
  (let [git-changes (:out (git ["status" "--porcelain"] {:out :capture}))]
    (when-not (string/blank? git-changes)
      (println git-changes)
      (die 12 "Git working directory must be clean. Run `git commit`")))
  params)

(defn- assert-scm-tagged [params]
  (when-not (zero? (:exit (git ["rev-list" tag] {:out :ignore})))
    (die 13 "\nGit tag %s must exist. Run `bin/tag-release`" tag))
  (let [{:keys [exit out]} (git ["describe" "--tags" "--abbrev=0" "--exact-match"] {:out :capture})]
    (when-not (and (zero? exit)
                   (= (string/trim out) tag))
      (die 14 (string/join "\n"
                           [""
                            "Git tag %s must be on HEAD."
                            ""
                            "Proceed with caution, because this tag may have already been released. If you've determined it's safe, run `git tag -d %s` before re-running `bin/tag-release`."]) tag tag)))
  params)

#_{:clj-kondo/ignore #{:clojure-lsp/unused-public-var}}
(defn tag-release "Tag the HEAD commit for the current release." [params]
  (when-not (zero? (:exit (git ["tag" tag] {})))
    (die 15 "\nCouldn't create tag %s." tag))
  params)

(defn clean "Remove the target folder." [params]
  (bb/clean params))

(defn jar "Build the library JAR file." [params]
  (bb/jar (assoc params
                 :lib     lib
                 :version version
                 :basis   basis
                 :tag     (git-rev))))

(defn check-release
  "Check that the library is ready to be released.

  * No outstanding commits
  * Git tag for current release exists in local repo
  * CHANGELOG.md references new tag
  * package.json references new tag"
  [params]
  (assert-changelog-updated params)
  (assert-package-json-updated params)
  ;; after assertions about content, so any change can be committed/amended
  (assert-scm-clean params)
  ;; last, so that correct commit is tagged
  (assert-scm-tagged params)
  params)

#_{:clj-kondo/ignore #{:clojure-lsp/unused-public-var}}
(defn release
  "Release the library.

  * Confirm that we are ready to release. See [[check-release]]
  * Build the JAR
  * Deploy to Clojars
  * Ensure the tag is available on Github"
  [params]
  (check-release params)
  (clean params)
  (jar params)
  (bb/deploy {:lib     lib
              :version version})
  (git-push params)
  params)
