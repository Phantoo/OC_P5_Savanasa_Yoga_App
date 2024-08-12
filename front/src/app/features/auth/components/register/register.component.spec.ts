import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Observable, startWith, throwError } from 'rxjs';
import { Router } from '@angular/router';

describe('RegisterComponent', () => {
	let component: RegisterComponent;
	let fixture: ComponentFixture<RegisterComponent>;
	let router: Router;
	
	const mockAuthService = {
		register: jest.fn()
	}
	
	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [RegisterComponent],
			imports: [
				BrowserAnimationsModule,
				HttpClientModule,
				ReactiveFormsModule,  
				MatCardModule,
				MatFormFieldModule,
				MatIconModule,
				MatInputModule
			],
			providers: [
				{ provide: AuthService, useValue: mockAuthService }
			]
		})
		.compileComponents();
		
		fixture = TestBed.createComponent(RegisterComponent);
		component = fixture.componentInstance;
		router = TestBed.inject(Router);
		fixture.detectChanges();
	});
	
	it('should create', () => {
		expect(component).toBeTruthy();
	});
	
	it('should not register and have error when request with an empty field is submitted', () => {
		jest.spyOn(mockAuthService, 'register').mockImplementation(r => throwError(() => new Error('')));
		component.form.setValue({ email: 'qsd@qsd.fr', firstName: '', lastName: 'QSD', password: 'pass'});
		component.submit();
		expect(mockAuthService.register).toHaveBeenCalled();
		expect(component.onError).toBe(true);
	})
	
	it('should not register and have error when request with a wrong email is submitted', () => {
		jest.spyOn(mockAuthService, 'register').mockImplementation(r => throwError(() => new Error('')));
		component.form.setValue({ email: 'qsd@qsd', firstName: 'qsd', lastName: 'QSD', password: 'pass'});
		component.submit();
		expect(mockAuthService.register).toHaveBeenCalled();
		expect(component.onError).toBe(true);
	})
	
	it('should register when a valid request is submitted', () => {
		let routerSpy = jest.spyOn(router, 'navigate');
		jest.spyOn(mockAuthService, 'register').mockImplementation(r => new Observable<any>().pipe(startWith(1)));
		component.form.setValue({ email: 'qsd@qsd.fr', firstName: 'qsd', lastName: 'QSD', password: 'pass'});
		component.submit();
		expect(mockAuthService.register).toHaveBeenCalled();
		expect(component.onError).toBe(false);
		expect(routerSpy).toHaveBeenCalledWith(['/login']);
	})
});
