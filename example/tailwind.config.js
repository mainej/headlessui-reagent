const defaultTheme = require('tailwindcss/defaultTheme');
const {defaultExtractor} = require('tailwindcss/lib/lib/defaultExtractor');

// In CLJS classes are either
// - hiccup style: :div.variant-a:class-a.variant-b:class-b
// - keyword style: :variant-a:class-a or [:variant-a:class-a :variant-b:class-b]
// - string style: "variant-a:class-a" or "variant-a:class-a variant-b:class-b" or ["variant-a:class-a" "variant-b:class-b"]
// The [default
// INNER_MATCH_GLOBAL_REGEXP](https://github.com/tailwindlabs/tailwindcss/blob/master/src/lib/defaultExtractor.js)
// fails to remove the leading colon from the keyword style, which this
// regexp fixes.
// const INNER_MATCH_GLOBAL_REGEXP = /[^<>"'`\s.(){}[\]#=%$]*[^<>"'`\s.(){}[\]#=%:$]/g
const INNER_MATCH_GLOBAL_REGEXP = /[^<>"'`\s.(){}[\]#=%$:][^<>"'`\s.(){}[\]#=%$]*[^<>"'`\s.(){}[\]#=%:$]/g;

// This helps, but in a few areas Tailwind syntax still can't be translated to
// hiccup. Because keywords can't contain square brackets, Tailwind's [arbitrary
// values](https://tailwindcss.com/docs/adding-custom-styles#using-arbitrary-values)
// must be placed in strings.
// [:h1 {:class "text-[500px]"}]

// Similarly, because keywords can't contain slashes, [opacity
// modifiers](https://tailwindcss.com/docs/text-color#changing-the-opacity) must
// be placed in strings.
// [:span {:class "text-white/50"}]
// or
// [:span.text-white.opacity-50]
// The same applies to fractional spacing:
// [:div {:class "w-1/2"}]

// And, because dots are special syntax for hiccup, decimal spacing must be
// placed in strings.
// [:div {:class "w-1.5"}]
function hiccupExtractor(content) {
  let defaultMatches = defaultExtractor(content);
  let innerMatches = content.match(INNER_MATCH_GLOBAL_REGEXP) || [];
  let results = [...defaultMatches, ...innerMatches].flat().filter((v) => v !== undefined);

  return results;
}

module.exports = {
  content: {
    files: [
      './resources/index.html',
      './src/main/**/*.cljs',
      './src/main/**/*.cljc',
    ],
    extract: {
      DEFAULT: hiccupExtractor
    },
  },
  theme: {
    extend: {
      fontFamily: {
        'sans': ['Inter var', ...defaultTheme.fontFamily.sans],
      },
    },
  },
  plugins: [],
};
