import "../support/commands";

describe('Account Spec', () => {
    it('should display account details', () => {
        cy.intercept('GET', '/api/session', []);
        cy.login(false);
        cy.intercept('GET', '/api/user/1', {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            email: 'test@t.fr',
            createdAt: new Date(Date.now()),
            updatedAt: new Date(Date.now())
        });
        cy.get('span').contains('Account').click();
        cy.url().should('include', '/me');
    });

    it('should delete account', () => {
        cy.intercept('GET', '/api/session', []);
        cy.login(false);
        cy.intercept('GET', '/api/user/1', {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            email: 'test@t.fr',
            createdAt: new Date(Date.now()),
            updatedAt: new Date(Date.now())
        }).as('user');
        cy.get('span').contains('Account').click();
        cy.url().should('include', '/me');
        cy.wait('@user')
        cy.intercept('DELETE', '/api/user/1', {});
        cy.get('button').contains('Detail').click();
        cy.get('snack-bar-container').contains('Your account has been deleted !').should('be.visible');
    });

    
    it('should go back to sessions', () => {
        cy.intercept('GET', '/api/session', []);
        cy.login(false);
        cy.intercept('GET', '/api/user/1', {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            email: 'test@t.fr',
            createdAt: new Date(Date.now()),
            updatedAt: new Date(Date.now())
        }).as('user');
        cy.get('span').contains('Account').click();
        cy.url().should('include', '/me');
        cy.wait('@user')
        cy.get('button').contains('arrow_back').click();
        cy.url().should('include', '/sessions');
    });
});