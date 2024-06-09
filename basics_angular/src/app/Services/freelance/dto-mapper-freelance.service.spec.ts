import { TestBed } from '@angular/core/testing';

import { DtoMapperFreelanceService } from './dto-mapper-freelance.service';

describe('DtoMapperFreelanceService', () => {
  let service: DtoMapperFreelanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DtoMapperFreelanceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
