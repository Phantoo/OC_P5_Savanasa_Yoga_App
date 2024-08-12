import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';

describe('AuthService', () => {
	let service: AuthService;
	let httpMock: HttpTestingController;

	const mockRegisterRequest: RegisterRequest = {
		email: 't@t.fr',
		firstName: 'test',
		lastName: 'test',
		password: 'test!1234'
	}

	const mockLoginRequest: LoginRequest = {
		email: 't@t.fr',
		password: 'test!1234'
	}

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
		TestBed.configureTestingModule({
			imports:[
				HttpClientTestingModule
			]
		});
		service = TestBed.inject(AuthService);
		httpMock = TestBed.inject(HttpTestingController);
	});
	
	afterEach(() => {
		httpMock.verify();
	});
	
	it('should be created', () => {
		expect(service).toBeTruthy();
	});
	
	it("should register account", () => {
		const mockResponse = new Observable<void>();
		service.register(mockRegisterRequest).subscribe((data) => {
			expect(data).toBe(mockResponse);
		})
		const req = httpMock.expectOne('api/auth/register');
		expect(req.request.method).toBe('POST');
		req.flush(mockResponse);
	});

	it("should log the account in", () => {
		const mockResponse = mockSessionInformation;
		service.login(mockLoginRequest).subscribe((data) => {
			expect(data).toBe(mockResponse);
		})
		const req = httpMock.expectOne('api/auth/login');
		expect(req.request.method).toBe('POST');
		req.flush(mockResponse);
	});
});
