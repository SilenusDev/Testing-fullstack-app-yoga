import { defineConfig } from 'cypress'

export default defineConfig({
  videosFolder: 'cypress/videos',
  screenshotsFolder: 'cypress/screenshots',
  fixturesFolder: 'cypress/fixtures',
  video: false,
    e2e: {
    // Enable experimental session and origin
    experimentalSessionAndOrigin: true,  // Ajout du flag pour activer les sessions

    // Nous avons importé tes anciens plugins cypress ici.
    // Tu souhaiteras peut-être nettoyer cela plus tard en important ces derniers.
    setupNodeEvents(on, config) {
	  require('@cypress/code-coverage/task')(on, config);
      return require('./cypress/plugins/index.ts').default(on, config)
    },
    baseUrl: 'http://localhost:4200',
  },
})

