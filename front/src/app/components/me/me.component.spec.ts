import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { expect } from '@jest/globals';
import { of, Observable, startWith } from 'rxjs';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { DebugElement } from '@angular/core';
import { Router } from '@angular/router';

describe('MeComponent', () => {
	let component: MeComponent;
	let fixture: ComponentFixture<MeComponent>;

	const mockSessionService = {
		sessionInformation: {
			admin: true,
			id: 1
		},
		isLogged: true,
		logOut: jest.fn()
	}

	const mockUserService = {
		getById: jest.fn(id => of({
			id: 1,
			email: "test@test.com",
			firstName: "test",
			lastName: "ostero",
			admin: true,
			password: "blabla",
			createdAt: new Date(Date.now()),
			updatedAt: new Date(Date.now())
		})),
		delete: jest.fn(_ => new Observable<any>().pipe(startWith(1)))
	};
	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [MeComponent],
			imports: [
				MatSnackBarModule,
				HttpClientModule,
				MatCardModule,
				MatFormFieldModule,
				MatIconModule,
				MatInputModule
			],
			providers: [
				{ provide: SessionService, useValue: mockSessionService }, 
				{ provide: UserService, useValue: mockUserService},
				{ provide: MatSnackBar, useValue: { open: jest.fn() }},
				{ provide: Router, useValue: { navigate: jest.fn() }}
			],
		})
		.compileComponents();

		fixture = TestBed.createComponent(MeComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});

	it('should load currently authentified user', () => {
		component.ngOnInit();
		expect(component.user).toBeDefined();
		expect(component.user?.firstName).toBe('test')
	})

	it('should display user info', () => {
		const debugElement: DebugElement = fixture.debugElement;
		const element: HTMLElement = debugElement.nativeElement;
		const infoElements = element.querySelectorAll('p');
		expect(infoElements).not.toBeNull();
		expect(infoElements.length).toBe(5);
		expect(infoElements[0].textContent).toEqual('Name: test OSTERO');
		expect(infoElements[1].textContent).toEqual('Email: test@test.com');
		expect(infoElements[2].textContent).toEqual('You are admin');
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

	it('should delete currently authentified user and log out', () => {
		jest.spyOn(component, 'delete');
		mockSessionService.logOut.mockImplementationOnce(() => null);
		component.delete();
		expect(mockSessionService.logOut).toHaveBeenCalled();
	});
});
