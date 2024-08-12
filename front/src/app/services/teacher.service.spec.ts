import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
	let service: TeacherService;
	let httpMock: HttpTestingController;

	const mockTeacher : Teacher = {
		id: 1,
		firstName: 'test',
		lastName: 'test',
		createdAt: new Date(Date.now()),
		updatedAt: new Date(Date.now())
	}

	const mockTeacher2 : Teacher = {
		id: 2,
		firstName: 'test 2',
		lastName: 'test 2',
		createdAt: new Date(Date.now()),
		updatedAt: new Date(Date.now())
	}
	
	beforeEach(() => {
		TestBed.configureTestingModule({
			imports:[
				HttpClientTestingModule
			]
		});
		service = TestBed.inject(TeacherService);
		httpMock = TestBed.inject(HttpTestingController)
	});
	
	it('should be created', () => {
		expect(service).toBeTruthy();
	});
	
	it("should return teachers after 'all' is called", () => {
		const mockResponse = [
			mockTeacher,
			mockTeacher2
		];
		service.all().subscribe((data) => {
			expect(data).toBe(mockResponse);
		})
		const req = httpMock.expectOne('api/teacher');
		expect(req.request.method).toBe('GET');
		req.flush(mockResponse);
	});

	it("should return teacher after 'detail' is called", () => {
		const mockResponse = mockTeacher;
		service.detail('1').subscribe((data) => {
			expect(data).toBe(mockResponse);
		})
		const req = httpMock.expectOne('api/teacher/1');
		expect(req.request.method).toBe('GET');
    	req.flush(mockResponse);
	});
});
