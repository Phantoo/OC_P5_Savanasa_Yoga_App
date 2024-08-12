describe('Register Spec', () => {
    it('Registration successfull', () => {
        cy.visit('/register');

        cy.intercept('POST', 'api/auth/register', { statusCode: 200 });

        cy.get('input[formControlName=firstName]').type("tot");
        cy.get('input[formControlName=lastName]').type("TOt");
        cy.get('input[formControlName=email]').type("t@t.com");
		cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

        cy.url().should('include', '/login');
    });

    it('Registration failure', () => {
        cy.visit('/register');

        cy.get('input[formControlName=firstName]').type("tot");
        cy.get('input[formControlName=lastName]').type("TOt");
        cy.get('input[formControlName=email]').type("t@t.com");
		cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
        
        cy.url().should('include', '/register');
        cy.get('.error').should('be.visible');
    })

    it('Registration failure : email already in use', () => {
        cy.visit('/register');

        cy.intercept('POST', 'api/auth/register', { statusCode: 400 });

        cy.get('input[formControlName=firstName]').type("tot");
        cy.get('input[formControlName=lastName]').type("TOt");
        cy.get('input[formControlName=email]').type("t@t.com");
		cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
        
        cy.url().should('include', '/register');
        cy.get('.error').should('be.visible');
    })

    
    it('Registration failure : field is empty', () => {
        cy.visit('/register');

        cy.intercept('POST', 'api/auth/register', { statusCode: 400 });

        cy.get('input[formControlName=firstName]').type("tot");
        cy.get('input[formControlName=email]').type("t@t.com");
		cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
        
        cy.url().should('include', '/register');
        cy.get('.error').should('not.be.exist');
        cy.get('button[type=submit]').should('be.disabled');
    })
});