import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
	let service: SessionService;

	const mockSessionInformation: SessionInformation = {
		token: 'blabla',
		type: 'type',
		id: 1,
		username: 'test',
		firstName: 'test',
		lastName: 'test',
		admin: false
	}
	
	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(SessionService);
	});
	
	it('should be created', () => {
		expect(service).toBeTruthy();
	});

	it("should log the account in", () => {
		service.$isLogged().subscribe((data) => {
			expect(data).toBe(true)
		})
		service.logIn(mockSessionInformation);
		expect(service.sessionInformation).toBe(mockSessionInformation);
		expect(service.isLogged).toBe(true);
	});

	it("should log the account out", () => {
		service.$isLogged().subscribe((data) => {
			expect(data).toBe(false)
		})
		service.logOut();
		expect(service.sessionInformation).toBe(undefined);
		expect(service.isLogged).toBe(false);
	});
});
