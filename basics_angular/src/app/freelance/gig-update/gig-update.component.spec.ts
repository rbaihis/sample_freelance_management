import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GigUpdateComponent } from './gig-update.component';

describe('GigUpdateComponent', () => {
  let component: GigUpdateComponent;
  let fixture: ComponentFixture<GigUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GigUpdateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GigUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
