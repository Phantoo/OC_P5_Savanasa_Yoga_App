import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of, startWith, Subject, Subscription } from 'rxjs';
import { Session } from '../../interfaces/session.interface';

describe('FormComponent', () => {
	let component: FormComponent;
	let fixture: ComponentFixture<FormComponent>;
	let router: Router;
	let activatedRoute: ActivatedRoute;
	
	const mockSessionService = {
		sessionInformation: {
			admin: false
		}
	} 

	const mockSessionApiService = {
		detail: jest.fn((id : string) => { of(mockSession) }),
		create: jest.fn((session : Session) => { of(session) }),
		update: jest.fn((id : string, session : Session) => { of(session) }),
		subscribe: jest.fn(),
	}

	const mockSession: Session = {
		id: 1,
		name: 'session',
		description: 'desc',
		users: [1],
		teacher_id: 1,
		date: new Date(Date.now())
	}

	const mockSessionForm = {
		name: mockSession.name,
		date: mockSession.date,
		teacher_id: mockSession.teacher_id,
		description: mockSession.description
	}
	
	beforeEach(async () => {
		await TestBed.configureTestingModule({
			
			imports: [
				RouterTestingModule,
				HttpClientModule,
				MatCardModule,
				MatIconModule,
				MatFormFieldModule,
				MatInputModule,
				ReactiveFormsModule, 
				MatSnackBarModule,
				MatSelectModule,
				BrowserAnimationsModule
			],
			providers: [
				{ provide: SessionService, useValue: mockSessionService },
				{ provide: SessionApiService, useValue: mockSessionApiService},
				{ provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: ()=> 1 } } } },
				{ provide: MatSnackBar, useValue: { open: jest.fn() }},
			],
			declarations: [FormComponent]
		})
		.compileComponents();
		
		fixture = TestBed.createComponent(FormComponent);
		component = fixture.componentInstance;
		router = TestBed.inject(Router);
		activatedRoute = TestBed.inject(ActivatedRoute);
		fixture.detectChanges();
	});
	
	it('should create', () => {
		expect(component).toBeTruthy();
	});
	
	it('should navigate to sessions if not admin', () => {
		let routerSpy = jest.spyOn(router, 'navigate');
		component.ngOnInit();
		expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
	});

	it("should be in update mode if url contains 'update'", () => {
		let sessionApiDetailSpy = jest.spyOn(mockSessionApiService, 'detail').mockImplementationOnce(session => of(mockSession));
		jest.spyOn(router, 'url', 'get').mockReturnValue('/update/1');
		component.ngOnInit();
		expect(component.onUpdate).toBe(true);
		expect(sessionApiDetailSpy).toHaveBeenCalledWith(1);
	});

	it('should create session when submit is called', () => {
		let sessionApiCreateSpy = jest.spyOn(mockSessionApiService, 'create').mockImplementationOnce(session => of(mockSession));
		let sessionApiUpdateSpy = jest.spyOn(mockSessionApiService, 'update');
		component.ngOnInit();
		component.sessionForm?.setValue(mockSessionForm);
		component.submit();
		expect(sessionApiCreateSpy).toHaveBeenCalledWith(mockSessionForm);
		expect(sessionApiUpdateSpy).not.toHaveBeenCalled();
	});

	it('should update session when submit is called and url contains update', () => {
		let sessionApiDetailSpy = jest.spyOn(mockSessionApiService, 'detail').mockImplementationOnce(session => of(mockSession));
		let sessionApiUpdateSpy = jest.spyOn(mockSessionApiService, 'update').mockImplementationOnce((id: string, session: Session) => of(mockSession));
		jest.spyOn(router, 'url', 'get').mockReturnValue('/update/1');
		component.ngOnInit();
		component.sessionForm?.setValue(mockSessionForm);
		component.submit();
		expect(sessionApiDetailSpy).toHaveBeenCalledWith(1);
		expect(sessionApiUpdateSpy).toHaveBeenCalledWith(1, mockSessionForm);
	});
});
