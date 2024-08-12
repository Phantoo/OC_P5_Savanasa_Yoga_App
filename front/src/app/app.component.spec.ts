import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { AuthService } from './features/auth/services/auth.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('AppComponent', () => {
	let component: AppComponent;
	let fixture: ComponentFixture<AppComponent>;
	let router: Router;

	const mockSessionService = {
		$isLogged: jest.fn(),
		logOut: jest.fn()
	}

	const mockAuthService = {}

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [
				RouterTestingModule,
				HttpClientModule,
				MatToolbarModule
			],
			declarations: [
				AppComponent
			],
			providers: [
				{ provide: SessionService, useValue: mockSessionService },
				{ provide: AuthService, useValue: mockAuthService }
			],
		}).compileComponents();
		fixture = TestBed.createComponent(AppComponent);
		router = TestBed.inject(Router);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});
	
	it('should create the app', () => {
		const fixture = TestBed.createComponent(AppComponent);
		const app = fixture.componentInstance;
		expect(app).toBeTruthy();
	});

	it('should return $isLogged as false', () => {
		let spy = jest.spyOn(mockSessionService, '$isLogged').mockImplementationOnce(() => of(false));
		component.$isLogged().subscribe((data) => {
			expect(data).toBe(false);
		})
		expect(spy).toHaveBeenCalled();
	});

	it('should return $isLogged as true', () => {
		let spy = jest.spyOn(mockSessionService, '$isLogged').mockImplementationOnce(() => of(true));
		component.$isLogged().subscribe((data) => {
			expect(data).toBe(true);
		});
		expect(spy).toHaveBeenCalled();
	});

	it('should log out', () => {
		let spy = jest.spyOn(mockSessionService, 'logOut');
		let routerSpy = jest.spyOn(router, 'navigate');
		component.logout();
		expect(spy).toHaveBeenCalled();
		expect(routerSpy).toHaveBeenCalledWith(['']);
	});
});
