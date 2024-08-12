import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { Observable } from 'rxjs';

describe('SessionsService', () => {
	let service: SessionApiService;
	let httpMock: HttpTestingController;

	const mockSession: Session = {
		id: 1,
		name: 'session',
		description: 'desc',
		users: [1],
		teacher_id: 1,
		date: new Date(Date.now())
	}
	
	const mockSession2: Session = {
		id: 2,
		name: 'session 2',
		description: 'desc 2',
		users: [],
		teacher_id: 1,
		date: new Date(Date.now())
	}

	beforeEach(() => {
		TestBed.configureTestingModule({
			imports:[
				HttpClientTestingModule
			]
		});
		service = TestBed.inject(SessionApiService);
		httpMock = TestBed.inject(HttpTestingController);
	});

	afterEach(() => {
		httpMock.verify();
	});
	
	it('should be created', () => {
		expect(service).toBeTruthy();
	});

	it("should return sessions after 'all' is called", () => {
		const mockResponse = [
			mockSession,
			mockSession2
		];
		service.all().subscribe((data) => {
			expect(data).toBe(mockResponse);
		})
		const req = httpMock.expectOne('api/session');
		expect(req.request.method).toBe('GET');
    	req.flush(mockResponse);
	});

	it("should return session after 'detail' is called", () => {
		const mockResponse = mockSession;
		service.detail('1').subscribe((data) => {
			expect(data).toBe(mockResponse);
		})
		const req = httpMock.expectOne('api/session/1');
		expect(req.request.method).toBe('GET');
    	req.flush(mockResponse);
	});

	it("should delete session", () => {
		const mockResponse = new Observable<any>();
		service.delete('1').subscribe((data) => {
			expect(data).toBe(mockResponse);
		})
		const req = httpMock.expectOne('api/session/1');
		expect(req.request.method).toBe('DELETE');
    	req.flush(mockResponse);
	});

	it("should create session", () => {
		const mockResponse = mockSession;
		service.create(mockSession).subscribe((data) => {
			expect(data).toBe(mockResponse);
		})
		const req = httpMock.expectOne('api/session');
		expect(req.request.method).toBe('POST');
    	req.flush(mockResponse);
	});

	it("should update session", () => {
		const mockResponse = mockSession;
		service.update('1', mockSession).subscribe((data) => {
			expect(data).toBe(mockResponse);
		})
		const req = httpMock.expectOne('api/session/1');
		expect(req.request.method).toBe('PUT');
    	req.flush(mockResponse);
	});

	it("should participate to session", () => {
		const mockResponse = new Observable<void>();
		service.participate('2', '1').subscribe((data) => {
			expect(data).toBe(mockResponse);
		})
		const req = httpMock.expectOne('api/session/2/participate/1');
		expect(req.request.method).toBe('POST');
    	req.flush(mockResponse);
	});

	it("should unparticipate to session", () => {
		const mockResponse = new Observable<void>();
		service.unParticipate('1', '1').subscribe((data) => {
			expect(data).toBe(mockResponse);
		})
		const req = httpMock.expectOne('api/session/1/participate/1');
		expect(req.request.method).toBe('DELETE');
    	req.flush(mockResponse);
	});
});
