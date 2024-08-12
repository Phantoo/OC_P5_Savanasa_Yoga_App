import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { User } from '../interfaces/user.interface';
import { Observable } from 'rxjs';

describe('UserService', () => {
	let service: UserService;
	let httpMock: HttpTestingController;

	const mockUser: User = {
		id: 1,
		firstName: 'test',
		lastName: 'test',
		admin: false,
		email: 't@t.fr',
		password: 'test!1234',
		createdAt: new Date(Date.now()),
		updatedAt: new Date(Date.now())
	}
	
	beforeEach(() => {
		TestBed.configureTestingModule({
			imports:[
				HttpClientTestingModule
			]
		});
		service = TestBed.inject(UserService);
		httpMock = TestBed.inject(HttpTestingController);
	});
	
	it('should be created', () => {
		expect(service).toBeTruthy();
	});

	it("should return user", () => {
		const mockResponse = mockUser;
		service.getById('1').subscribe((data) => {
			expect(data).toBe(mockResponse);
		})
		const req = httpMock.expectOne('api/user/1');
		expect(req.request.method).toBe('GET');
    	req.flush(mockResponse);
	});

	it("should delete user", () => {
		const mockResponse = new Observable<any>();
		service.delete('1').subscribe((data) => {
			expect(data).toBe(mockResponse);
		})
		const req = httpMock.expectOne('api/user/1');
		expect(req.request.method).toBe('DELETE');
    	req.flush(mockResponse);
	});

});
