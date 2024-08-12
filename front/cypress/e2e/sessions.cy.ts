import "../support/commands";

describe('Sessions Spec', () => {

    it('should display sessions', () => {
        cy.intercept('GET', '/api/session',[
            {
                "id": 1,
                "name": "Test",
                "date": "2024-07-25T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "Test",
                "users": [],
                "createdAt": "2024-07-19T11:30:09",
                "updatedAt": "2024-07-22T16:42:27"
            },
            {
                "id": 2,
                "name": "Test 2",
                "date": "2024-07-25T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "Test 2",
                "users": [],
                "createdAt": "2024-07-19T11:30:09",
                "updatedAt": "2024-07-22T16:42:27"
            }
        ]).as('sessions');
        cy.login(false);
        cy.wait('@sessions');
        cy.url().should('include', '/sessions');
        cy.get('mat-card').should('have.length', 3) // Container + 2 sessions
    });

    it('should show session details', () => {
        cy.intercept('GET', '/api/session',[
            {
                "id": 1,
                "name": "Test",
                "date": "2024-07-25T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "Test",
                "users": [],
                "createdAt": "2024-07-19T11:30:09",
                "updatedAt": "2024-07-22T16:42:27"
            },
            {
                "id": 2,
                "name": "Test 2",
                "date": "2024-07-25T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "Test 2",
                "users": [],
                "createdAt": "2024-07-19T11:30:09",
                "updatedAt": "2024-07-22T16:42:27"
            }
        ]).as('sessions');
        cy.login(true);
        cy.wait('@sessions')
        cy.intercept('GET', '/api/session/1', {
            "id": 1,
            "name": "Test",
            "date": "2024-07-25T00:00:00.000+00:00",
            "teacher_id": 1,
            "description": "Test",
            "users": [],
            "createdAt": "2024-07-19T11:30:09",
            "updatedAt": "2024-07-22T16:42:27"
        });
        cy.get('button').contains('Detail').click();
        cy.url().should('include', '/sessions/detail/1');
    });
    
    it('should delete session', () => {
        cy.intercept('GET', '/api/session',[
            {
                "id": 1,
                "name": "Test",
                "date": "2024-07-25T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "Test",
                "users": [],
                "createdAt": "2024-07-19T11:30:09",
                "updatedAt": "2024-07-22T16:42:27"
            },
            {
                "id": 2,
                "name": "Test 2",
                "date": "2024-07-25T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "Test 2",
                "users": [],
                "createdAt": "2024-07-19T11:30:09",
                "updatedAt": "2024-07-22T16:42:27"
            }
        ]).as('sessions');
        cy.login(true);
        cy.wait('@sessions')
        cy.intercept('GET', '/api/session/1', {
            "id": 1,
            "name": "Test",
            "date": "2024-07-25T00:00:00.000+00:00",
            "teacher_id": 1,
            "description": "Test",
            "users": [],
            "createdAt": "2024-07-19T11:30:09",
            "updatedAt": "2024-07-22T16:42:27"
        });
        cy.get('button').contains('Detail').click();
        cy.url().should('include', '/sessions/detail/1');
        cy.intercept('DELETE', '/api/session/1',{ statusCode: 200 });
        cy.get('button').contains('Delete').click();
        cy.url().should('include', '/sessions');
        cy.get('snack-bar-container').contains('Session deleted !').should('be.visible');
    });

    it('should update session', () => {
        cy.intercept('GET', '/api/session',[
            {
                "id": 1,
                "name": "Test",
                "date": "2024-07-25T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "Test",
                "users": [],
                "createdAt": "2024-07-19T11:30:09",
                "updatedAt": "2024-07-22T16:42:27"
            },
            {
                "id": 2,
                "name": "Test 2",
                "date": "2024-07-25T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "Test 2",
                "users": [],
                "createdAt": "2024-07-19T11:30:09",
                "updatedAt": "2024-07-22T16:42:27"
            }
        ]).as('sessions');
        cy.login(true);
        cy.wait('@sessions')
        cy.intercept('GET', '/api/session/1', {
            "id": 1,
            "name": "Test",
            "date": "2024-07-25T00:00:00.000+00:00",
            "teacher_id": 1,
            "description": "Test",
            "users": [],
            "createdAt": "2024-07-19T11:30:09",
            "updatedAt": "2024-07-22T16:42:27"
        });
        cy.get('button').contains('Edit').click();
        cy.url().should('include', '/sessions/update/1');
        cy.get('input[formControlName=name]').type('{moveToEnd} new');
        cy.intercept('PUT', '/api/session/1', { statusCode: 200 });
        cy.get('button[type=submit]').click();
        cy.url().should('include', '/sessions');
        cy.url().should('not.include', '/update');
        cy.get('snack-bar-container').contains('Session updated !').should('be.visible');
    });

    it('should go back to sessions from detail page', () => {
        cy.intercept('GET', '/api/session',[
            {
                "id": 1,
                "name": "Test",
                "date": "2024-07-25T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "Test",
                "users": [],
                "createdAt": "2024-07-19T11:30:09",
                "updatedAt": "2024-07-22T16:42:27"
            },
            {
                "id": 2,
                "name": "Test 2",
                "date": "2024-07-25T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "Test 2",
                "users": [],
                "createdAt": "2024-07-19T11:30:09",
                "updatedAt": "2024-07-22T16:42:27"
            }
        ]).as('sessions');
        cy.login(true);
        cy.wait('@sessions')
        cy.intercept('GET', '/api/session/1', {
            "id": 1,
            "name": "Test",
            "date": "2024-07-25T00:00:00.000+00:00",
            "teacher_id": 1,
            "description": "Test",
            "users": [],
            "createdAt": "2024-07-19T11:30:09",
            "updatedAt": "2024-07-22T16:42:27"
        });
        cy.get('button').contains('Detail').click();
        cy.url().should('include', '/sessions/detail/1');
        cy.get('button').contains('arrow_back').click();
        cy.url().should('include', '/sessions');
    });
    
    it('should display create session button when logged in as admin', () => {
        cy.intercept('GET', '/api/session',[]);
        cy.login(true);
        cy.url().should('include', '/sessions');
        cy.get('.mat-raised-button').contains('Create').should('be.visible');
    });

    it('should display create session form when logged in as admin and clicking on the create button', () => {
        cy.intercept('GET', '/api/session',[]);
        cy.login(true);
        cy.intercept('GET', '/api/teacher', []);
        cy.get('.mat-raised-button').click();
        cy.url().should('include', '/sessions/create');
    });

    it('should create session', () => {
        cy.intercept('GET', '/api/session',[]);
        cy.login(true);
        cy.intercept('GET', '/api/teacher', [
            {
                "id": 1,
                "lastName": '1',
                "firstName": 'Teacher',
                "createdAt": new Date(Date.now()),
                "updatedAt": new Date(Date.now())
            },
            {
                "id": 2,
                "lastName": '2',
                "firstName": 'Teacher',
                "createdAt": new Date(Date.now()),
                "updatedAt": new Date(Date.now())
            }
        ]);
        cy.get('.mat-raised-button').click();
        cy.get('input[formControlName=name]').type('New Session');
		cy.get('input[formControlName=date]').type('1998-01-30');
		cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Teacher 1').click();
		cy.get('textarea[formControlName=description]').type('New Description');
        cy.intercept('POST', '/api/session', {});
		cy.get('button[type=submit]').click();
        cy.intercept('GET', '/api/session', []);
        cy.url().should('include', '/sessions');
        cy.url().should('not.include', '/create');
        cy.get('snack-bar-container').contains('Session created !').should('be.visible');
    });

    it('should fail creating a session', () => {
        cy.intercept('GET', '/api/session',[]);
        cy.login(true);
        cy.intercept('GET', '/api/teacher', [
            {
                "id": 1,
                "lastName": '1',
                "firstName": 'Teacher',
                "createdAt": new Date(Date.now()),
                "updatedAt": new Date(Date.now())
            },
            {
                "id": 2,
                "lastName": '2',
                "firstName": 'Teacher',
                "createdAt": new Date(Date.now()),
                "updatedAt": new Date(Date.now())
            }
        ]).as('teachers');
        cy.get('.mat-raised-button').click();
		cy.get('button[type=submit]').should('be.disabled');
    });


});