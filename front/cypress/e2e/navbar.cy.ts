describe('App component spec', () => {

    // Test 1 : Vérifie le comportement de la barre de navigation lorsque l'utilisateur n'est PAS connecté
    it('Nav bar component not login', () => {
        cy.visit('/register') // Accède à la page d'inscription

        // Vérifie que les éléments de la barre de navigation sont présents
        cy.get('span').contains('Yoga app').should('exist')  // Le titre de l'application "Yoga app" doit exister
        cy.get('span').contains('Login').should('exist')     // Le bouton "Login" doit être visible
        cy.get('span').contains('Register').should('exist')  // Le bouton "Register" doit être visible

        // Test de navigation : clique sur "Login" et vérifie que l'URL change correctement
        cy.get('span').contains('Login').click()
        cy.url().should('include', '/login') // Vérifie que l'URL contient "/login"

        // Test de navigation : clique sur "Register" et vérifie que l'URL revient sur la page d'inscription
        cy.get('span').contains('Register').click()
        cy.url().should('include', '/register') // Vérifie que l'URL contient "/register"
    });

    // Test 2 : Vérifie le comportement de la barre de navigation lorsque l'utilisateur est connecté
    it('Nav bar component login', () => {
      cy.visit('/login') // Accède à la page de connexion

      // Intercepte la requête POST de connexion pour simuler une réponse de l'API (mock de la réponse serveur)
      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'userName',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: true // Simule un utilisateur avec des droits administrateur
        },
      })

      // Intercepte la requête GET pour vérifier la session utilisateur (simulation d'une session vide)
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        [] // La réponse simulée est un tableau vide
      ).as('session') // Alias pour réutiliser cette interception plus tard si nécessaire

      // Remplit le formulaire de connexion avec des identifiants simulés
      cy.get('input[formControlName=email]').type("yoga@studio.com") // Saisit l'email
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`) // Saisit le mot de passe et valide le formulaire

      // Vérifie que l'utilisateur est redirigé vers la page des sessions après connexion
      cy.url().should('include', '/sessions')

      // Vérifie que les éléments de la barre de navigation après connexion sont bien visibles
      cy.get('span').contains('Yoga app').should('exist')   // Le titre "Yoga app" doit toujours être visible
      cy.get('span').contains('Sessions').should('exist')   // Le lien "Sessions" doit être présent
      cy.get('span').contains('Account').should('exist')    // Le lien "Account" pour accéder au profil utilisateur doit être visible
      cy.get('span').contains('Logout').should('exist')     // Le bouton "Logout" doit être présent pour se déconnecter

      // Test de navigation : clique sur "Sessions" et vérifie la redirection
      cy.get('span').contains('Sessions').click()
      cy.url().should('include', '/sessions')

      // Test de navigation : clique sur "Account" pour accéder au profil de l'utilisateur
      cy.get('span').contains('Account').click()
      cy.url().should('include', '/me') // Vérifie que l'URL correspond à la page de profil utilisateur

      // Test de déconnexion : clique sur "Logout" et vérifie que l'utilisateur est redirigé vers la page d'accueil
      cy.get('span').contains('Logout').click()
      cy.url().should('include', '/') // Vérifie que l'URL est bien celle de la page d'accueil
    });
});
