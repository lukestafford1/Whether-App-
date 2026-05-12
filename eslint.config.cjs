const js = require("@eslint/js");

module.exports = [
  js.configs.recommended,

  // Browser files
  {
    files: ["frontend/js/*.js"],
    languageOptions: {
      globals: {
        window: "readonly",
        document: "readonly",
        console: "readonly",
        fetch: "readonly",
        localStorage: "readonly"
      }
    }
  },

  // Test / Node files
  {
    files: ["**/*.test.js"],
    languageOptions: {
      globals: {
        require: "readonly",
        global: "readonly",
        console: "readonly",
        module: "readonly"
      }
    }
  },

  {
    rules: {
      semi: ["error", "always"],
      "no-unused-vars": "error"
    }
  }
];