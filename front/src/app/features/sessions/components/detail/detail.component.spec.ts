import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { Observable, of, startWith } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { TeacherService } from 'src/app/services/teacher.service';
import { DebugElement } from '@angular/core';
import { Router, RouterModule } from '@angular/router';


describe('DetailComponent', () => {
	let component: DetailComponent;
	let fixture: ComponentFixture<DetailComponent>; 
	let service: SessionService;
	let router: Router;

	const mockTeacher: Teacher = {
		id: 1,
		firstName: 'tea',
		lastName: 'CHER',
		createdAt: new Date(Date.now()),
		updatedAt: new Date(Date.now()),
	}

	const mockSession: Session = {
		id: 1,
		name: 'session',
		description: 'desc',
		users: [1],
		teacher_id: 1,
		date: new Date(Date.now())
	}
	
	const mockSessionService = {
		sessionInformation: {
			admin: true,
			id: 1
		}
	}
	
	const mockSessionApiService = {
		detail: jest.fn((id) => new Observable<Session>().pipe(startWith(mockSession))),
		participate: jest.fn((sessionid, userId) => new Observable<void>().pipe(startWith(1))),
		unParticipate: jest.fn((sessionid, userId) => new Observable<void>().pipe(startWith(1))),
		delete: jest.fn((id) => new Observable<void>().pipe(startWith(1)))
	}

	const mockTeacherService = {
		detail: jest.fn(id => new Observable<Teacher>().pipe(startWith(mockTeacher)))
	}
	
	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [
				RouterTestingModule,
				HttpClientModule,
				MatSnackBarModule,
				ReactiveFormsModule
			],
			declarations: [DetailComponent], 
			providers: [
				{ provide: SessionService, useValue: mockSessionService },
				{ provide: SessionApiService, useValue: mockSessionApiService },
				{ provide: TeacherService, useValue: mockTeacherService },
				{ provide: MatSnackBar, useValue: { open: jest.fn() }},
			],
		})
		.compileComponents();
		service = TestBed.inject(SessionService);
		fixture = TestBed.createComponent(DetailComponent);
		router = TestBed.inject(Router);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});
	
	it('should create', () => {
		expect(component).toBeTruthy();
	});
	
	it('should fetch session, participation and teacher at initialization', () => {
		jest.spyOn(mockSessionApiService, 'detail');
		mockSession.users = [1];
		component.ngOnInit();
		expect(mockSessionApiService.detail).toHaveBeenCalled();
		expect(component.session).toEqual(mockSession);
		expect(component.isParticipate).toBe(true);
		expect(component.teacher).toEqual(mockTeacher);
	});

	it('should add user to session user list when participate', () => {
		let mockSession2 : Session = {
			id: 2,
			name: 'session 2',
			description: 'desc',
			users: [],
			teacher_id: 1,
			date: new Date(Date.now())
		}
		jest.spyOn(mockSessionApiService, 'participate');
		jest.spyOn(mockSessionApiService, 'detail').mockImplementationOnce(id => new Observable<Session>().pipe(startWith(mockSession2)));
		component.ngOnInit();
		expect(component.isParticipate).toBe(false);
		component.participate();
		expect(mockSessionApiService.participate).toHaveBeenCalled();
		expect(mockSessionApiService.detail).toHaveBeenCalled();
		expect(mockSession.users).toContain(1);
		expect(component.isParticipate).toBe(true);
	});

	it('should remove user to session user list when unparticipate', () => {
		let detailSpy = jest.spyOn(mockSessionApiService, 'detail')
		jest.spyOn(mockSessionApiService, 'unParticipate');
		component.ngOnInit();
		expect(component.isParticipate).toBe(true);
		let mockSession2 : Session = mockSession;
		mockSession2.users = [];
		detailSpy.mockImplementationOnce(id => new Observable<Session>().pipe(startWith(mockSession2)));
		component.unParticipate();
		expect(mockSessionApiService.unParticipate).toHaveBeenCalled();
		expect(mockSessionApiService.detail).toHaveBeenCalled();
		expect(mockSession.users).not.toContain(1);
		expect(component.isParticipate).toBe(false);
	});

	it('should go back to the previous window when Back Button is clicked', () => {
		jest.spyOn(component, 'back');
		const debugElement: DebugElement = fixture.debugElement;
		const element: HTMLElement = debugElement.nativeElement;
		const backButtonElement = element.querySelector('button');
		expect(backButtonElement).not.toBeNull();
		backButtonElement?.click();
		expect(component.back).toHaveBeenCalled();
	});

	it('should delete current session and go back to sessions page ', () => {
		jest.spyOn(component, 'delete');
		let routerSpy = jest.spyOn(router, 'navigate');
		component.delete();
		expect(component.delete).toHaveBeenCalled();
		expect(routerSpy).toHaveBeenCalledWith(['sessions']);
	});
});

