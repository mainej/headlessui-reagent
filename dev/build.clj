(ns build
  (:require
   [clojure.java.io :as jio]
   [clojure.string :as string]
   [clojure.tools.build.api :as b]
   [deps-deploy.deps-deploy :as d]))

(def ^:private lib 'com.github.mainej/headlessui-reagent)
(def ^:private git-revs (Integer/parseInt (b/git-count-revs nil)))
(defn- format-version [revision] (format "1.4.0.%s" revision))
(def ^:private version (format-version git-revs))
(def ^:private next-version (format-version (inc git-revs)))
(def ^:private tag (str "v" version))
(def ^:private next-tag (str "v" next-version))
(def ^:private class-dir "target/classes")
(def ^:private basis (b/create-basis {:root    nil
                                      :user    nil
                                      :project "deps.edn"}))
(def ^:private jar-file (format "target/%s-%s.jar" (name lib) version))
(def ^:private pom-dir (jio/file (b/resolve-path class-dir) "META-INF" "maven" (namespace lib) (name lib)))

(defn clean "Remove the target folder." [params]
  (b/delete {:path "target"})
  params)

(defn- die
  ([code message & args]
   (die code (apply format message args)))
  ([code message]
   (binding [*out* *err*]
     (println message))
   (System/exit code)))

(defn- git-rev []
  (let [{:keys [exit out]} (b/process {:command-args ["git" "rev-parse" "HEAD"]
                                       :out          :capture})]
    (when (zero? exit)
      (string/trim out))))

(defn jar "Build the library JAR file." [params]
  (b/write-pom {:class-dir class-dir
                :lib       lib
                :version   version
                :scm       {:tag (git-rev)}
                :basis     basis
                :src-dirs  ["src"]})
  (b/copy-dir {:src-dirs   ["src" "resources"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file  jar-file})
  params)

(defn- assert-changelog-updated [params]
  (when-not (string/includes? (slurp "CHANGELOG.md") tag)
    (die 10 (string/join "\n"
                         ["CHANGELOG.md must include tag."
                          "  * If you will amend the current commit, use %s"
                          "  * If you intend to create a new commit, use %s"]) version next-version))
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
  (when-not (-> {:command-args ["git" "status" "--porcelain"]
                 :out          :capture}
                b/process
                :out
                string/blank?)
    (die 12 "Git working directory must be clean. Run git commit"))
  params)

(defn- assert-scm-tagged [params]
  (when-not (-> {:command-args ["git" "rev-list" tag]
                 :out          :ignore
                 :err          :ignore}
                b/process
                :exit
                zero?)
    (die 13 "Git tag %s must exist. Run bin/tag-release" tag))
  (let [{:keys [exit out]} (b/process {:command-args ["git" "describe" "--tags" "--abbrev=0" "--exact-match"]
                                       :out          :capture})]
    (when (or (not (zero? exit))
              (not= (string/trim out) tag))
      (die 14 (string/join "\n"
                           ["Git tag %s must be on HEAD."
                            ""
                            "Proceed with caution, because this tag may have already been released. If you've determined it's safe, run `git tag -d %s` before re-running `bin/tag-release`."]) tag tag)))
  params)

(defn- git-push [params]
  (when (or (-> {:command-args ["git" "push" "origin" tag]
                 :out          :ignore
                 :err          :ignore}
                b/process
                :exit
                zero?
                not)
            (-> {:command-args ["git" "push" "origin"]
                 :out          :ignore
                 :err          :ignore}
                b/process
                :exit
                zero?
                not))
    (die 15 "Couldn't sync with github."))
  params)

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

  * Confirm that we are ready to release
  * Build the JAR
  * Deploy to Clojars
  * Ensure the tag is available on Github"
  [params]
  (check-release params)
  (jar params)
  (d/deploy {:installer :remote
             :artifact  jar-file
             :pom-file  (jio/file pom-dir "pom.xml")})
  (git-push params)
  params)

(comment
  (clean nil)
  (jar nil)
  (git-push nil)
  (assert-changelog-updated nil)
  )
