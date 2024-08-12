describe('Not Found Spec', () => {
    it('should display the Not Found page', () => {
        cy.visit('/testwiefowienfsadf');
        cy.url().should('include', '/404');
    });
});