import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GigDisplayComponent } from './gig-display.component';

describe('GigElementComponent', () => {
  let component: GigDisplayComponent;
  let fixture: ComponentFixture<GigDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GigDisplayComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GigDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
