import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationDisplayComponent } from './application-display.component';

describe('ApplicationDisplayComponent', () => {
  let component: ApplicationDisplayComponent;
  let fixture: ComponentFixture<ApplicationDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ApplicationDisplayComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ApplicationDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
