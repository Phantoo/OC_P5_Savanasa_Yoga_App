import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { Observable, startWith, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';

describe('LoginComponent', () => {
	let component: LoginComponent;
	let fixture: ComponentFixture<LoginComponent>;

	const mockSessionService = {
		isLogged: false,
		logIn: jest.fn()
	}

	const mockAuthService = {
		login: jest.fn()
	}

	beforeEach(async () => {
		await TestBed.configureTestingModule({
		declarations: [LoginComponent],
		providers: [
			{ provide: SessionService, useValue: mockSessionService },
			{ provide: AuthService, useValue: mockAuthService }
		],
		imports: [
			RouterTestingModule,
			BrowserAnimationsModule,
			HttpClientModule,
			MatCardModule,
			MatIconModule,
			MatFormFieldModule,
			MatInputModule,
			ReactiveFormsModule]
		})
		.compileComponents();
		fixture = TestBed.createComponent(LoginComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});

	const errorWithTimestamp$ = throwError(() => {
		const error: any = new Error('');
		error.timestamp = Date.now();
		return error;
	  });

	it('should not log in and have error when wrong credentials are submitted', () => {
		jest.spyOn(mockAuthService, 'login').mockImplementation(r => throwError(() => new Error('')));
		component.form.setValue({ email: 'qsd@qsd', password: 'qsd'});
		component.submit();
		expect(mockAuthService.login).toHaveBeenCalled();
		expect(mockSessionService.logIn).not.toHaveBeenCalled();
		expect(component.onError).toBe(true);
	})

	it('should log in when correct credentials are submitted', () => {
		jest.spyOn(mockAuthService, 'login').mockImplementation(r => new Observable<any>().pipe(startWith(1)));
		component.form.setValue({ email: 'yoga@studio.com', password: 'test!1234'});
		component.submit();
		expect(mockAuthService.login).toHaveBeenCalled();
		expect(mockSessionService.logIn).toHaveBeenCalled();
		expect(component.onError).toBe(false);
	})
});
