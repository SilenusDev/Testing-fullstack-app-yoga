describe('Register spec', () => {
    beforeEach(() => {
        // Accède à la page d'inscription avant chaque test
        cy.visit('/register');
    });

    it('Inscription réussie', () => {
        // Simulation d'une réponse API de succès pour l'inscription
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 200,
        }).as('registerRequest');

        // Remplit le formulaire d'inscription avec des données valides
        cy.get('input[formControlName="firstName"]').type('John');
        cy.get('input[formControlName="lastName"]').type('Doe');
        cy.get('input[formControlName="email"]').type('test@user.com');
        cy.get('input[formControlName="password"]').type('securePassword123');

        // Vérifie que le bouton de soumission est activé
        cy.get('button[type="submit"]').should('not.be.disabled');

        // Soumet le formulaire
        cy.get('button[type="submit"]').click();

        // Attend la réponse de l'API d'inscription
        cy.wait('@registerRequest');

        // Vérifie que l'utilisateur est redirigé vers la page de connexion après l'inscription
        cy.url().should('include', '/login');
    });

    it('Échec de l\'inscription (erreur serveur)', () => {
        // Simulation d'une réponse API d'erreur pour l'inscription (par exemple, email déjà utilisé)
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 400,
            body: {
                error: 'Email déjà utilisé',
            },
        }).as('registerFailed');

        // Remplit le formulaire d'inscription avec des données valides mais un email en doublon
        cy.get('input[formControlName="firstName"]').type('Jane');
        cy.get('input[formControlName="lastName"]').type('Doe');
        cy.get('input[formControlName="email"]').type('duplicate@user.com');
        cy.get('input[formControlName="password"]').type('securePassword123');

        // Vérifie que le bouton de soumission est activé
        cy.get('button[type="submit"]').should('not.be.disabled');

        // Soumet le formulaire
        cy.get('button[type="submit"]').click();

        // Attend la réponse de l'API d'inscription échouée
        cy.wait('@registerFailed');

        // Vérifie que le message d'erreur est affiché
        cy.get('.error').should('be.visible').and('contain', 'An error occurred');
    });

    it('Erreurs de validation du formulaire', () => {
        // Vérifie que les champs du formulaire sont invalides lorsqu'ils sont vides
        cy.get('input[formControlName="firstName"]').should('have.class', 'ng-invalid');
        cy.get('input[formControlName="lastName"]').should('have.class', 'ng-invalid');
        cy.get('input[formControlName="email"]').should('have.class', 'ng-invalid');
        cy.get('input[formControlName="password"]').should('have.class', 'ng-invalid');

        // Vérifie que le bouton de soumission est désactivé en raison de la validation du formulaire
        cy.get('button[type="submit"]').should('be.disabled');
    });

    it('Doit marquer les champs comme invalides et en rouge s\'ils sont vides', () => {
        // Active les validations en tentant de soumettre des champs vides
        cy.get('input[formControlName="firstName"]').focus().blur();
        cy.get('input[formControlName="lastName"]').focus().blur();
        cy.get('input[formControlName="email"]').focus().blur();
        cy.get('input[formControlName="password"]').focus().blur();

        // Vérifie que chaque champ a une bordure rouge indiquant une erreur de validation
        cy.get('mat-form-field').eq(0).should('have.class', 'mat-form-field-invalid');
        cy.get('mat-form-field').eq(1).should('have.class', 'mat-form-field-invalid');
        cy.get('mat-form-field').eq(2).should('have.class', 'mat-form-field-invalid');
        cy.get('mat-form-field').eq(3).should('have.class', 'mat-form-field-invalid');
    });
});
