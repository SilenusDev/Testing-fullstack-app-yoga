describe('Spécification de la connexion', () => {
  it('Connexion réussie', () => {
    // Accéder à la page de connexion
    cy.visit('/login');

    // Simuler une réponse d'API de connexion réussie
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200, // Code de statut HTTP indiquant le succès de la connexion
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
        token: 'fake-jwt-token', // Jeton fictif pour simuler l'authentification
      },
    }).as('loginRequest'); // Attribution d'un alias pour suivre cette requête

    // Remplir le formulaire de connexion
    cy.get('input[formControlName=email]').type('yoga@studio.com'); // Saisie de l'email
    cy.get('input[formControlName=password]').type('test!1234');   // Saisie du mot de passe

    // Soumettre le formulaire de connexion
    cy.get('button[type=submit]').click();

    // Attendre la réponse de la requête de connexion simulée
    cy.wait('@loginRequest');

    // Vérifier que l'URL a changé pour refléter une connexion réussie
    cy.url().should('include', '/sessions');
  });

  it('Connexion échouée', () => {
    // Accéder à la page de connexion
    cy.visit('/login');

    // Simuler une réponse d'API de connexion échouée
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401, // Code de statut HTTP indiquant une erreur d'authentification
      body: {
        error: 'Invalid credentials', // Message d'erreur retourné par l'API
      },
    }).as('loginFailure'); // Alias pour suivre la requête échouée

    // Remplir le formulaire avec des identifiants incorrects
    cy.get('input[formControlName=email]').type('wrong@user.com');
    cy.get('input[formControlName=password]').type('wrongpassword');

    // Soumettre le formulaire de connexion
    cy.get('button[type=submit]').click();

    // Attendre la réponse de la requête échouée
    cy.wait('@loginFailure');

    // Vérifier que l'utilisateur reste sur la page de connexion
    cy.url().should('include', '/login');

    // Vérifier la présence d'un message d'erreur
    cy.contains('An error occurred').should('be.visible');
  });

  it('Déconnexion de l’utilisateur', () => {
    // Accéder à la page de connexion
    cy.visit('/login');

    // Simuler une réponse d'API de connexion réussie
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
        token: 'fake-jwt-token',
      },
    }).as('loginRequest');

    // Remplir le formulaire de connexion
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');

    // Soumettre le formulaire
    cy.get('button[type=submit]').click();

    // Attendre la réponse de la requête de connexion
    cy.wait('@loginRequest');

    // Cliquer sur le lien de déconnexion
    cy.get('span.link').contains('Logout').click();

    // Vérifier que le lien de connexion est à nouveau visible après la déconnexion
    cy.get('span[routerlink="login"].link').should('be.visible');
  });

  it('Affichage d’une bordure rouge si le champ email est vide', () => {
    // Accéder à la page de connexion
    cy.visit('/login');

    // Focus sur le champ email puis perte de focus sans saisie
    cy.get('input[formControlName=email]').focus().blur();

    // Vérifier que le champ email est marqué comme invalide
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');

    // Vérifier que le champ parent possède l'état d'erreur
    cy.get('input[formControlName=email]')
      .parents('.mat-form-field')
      .should('have.class', 'mat-form-field-invalid');

    // Vérifier que le bouton de soumission est désactivé
    cy.get('button[type=submit]').should('be.disabled');
  });

  it('Affichage d’une bordure rouge si le champ mot de passe est vide', () => {
    // Accéder à la page de connexion
    cy.visit('/login');

    // Focus sur le champ mot de passe puis perte de focus sans saisie
    cy.get('input[formControlName=password]').focus().blur();

    // Vérifier que le champ mot de passe est marqué comme invalide
    cy.get('input[formControlName=password]').should('have.class', 'ng-invalid');

    // Vérifier que le champ parent possède l'état d'erreur
    cy.get('input[formControlName=password]')
      .parents('.mat-form-field')
      .should('have.class', 'mat-form-field-invalid');

    // Vérifier que le bouton de soumission est désactivé
    cy.get('button[type=submit]').should('be.disabled');
  });
});