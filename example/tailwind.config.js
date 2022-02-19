const defaultTheme = require('tailwindcss/defaultTheme')

module.exports = {
  content: {
    files: [
      './resources/index.html',
      './src/main/**/*.cljs',
      './src/main/**/*.cljc',
    ],
    extract: {
      // default: /[^<>"'`\s.(){}[\]#=%]*[^<>"'`\s.(){}[\]#=%:]/g
      // In CLJS classes are either
      // - hiccup style: :div.variant-a:class-a.variant-b:class-b
      // - keyword style: :variant-a:class-a
      // - string style: "variant-a:class-a variant-b:class-b"
      // The default fails to remove the leading colon from the keyword style
      DEFAULT: content => content.match(/[^<>"'`\s.(){}[\]#=%:][^<>"'`\s.(){}[\]#=%]*[^<>"'`\s.(){}[\]#=%:]/g) || []
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
}
