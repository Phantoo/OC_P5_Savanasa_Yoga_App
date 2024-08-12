import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { NotFoundComponent } from './not-found.component';
import { DebugElement } from '@angular/core';

describe('NotFoundComponent', () => {
	let component: NotFoundComponent;
	let fixture: ComponentFixture<NotFoundComponent>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [ NotFoundComponent ]
		})
		.compileComponents();

		fixture = TestBed.createComponent(NotFoundComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});

	it('should display "Page not found !"', () => {
		const debugElement: DebugElement = fixture.debugElement;
		const element: HTMLElement = debugElement.nativeElement;
		const textElement = element.querySelector('h1')!;
		expect(textElement.textContent).toEqual('Page not found !')
	});
});
