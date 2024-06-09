import { TestBed } from '@angular/core/testing';

import { FooConnectedUserService } from './foo-connected-user.service';

describe('FooConnectedUserService', () => {
  let service: FooConnectedUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FooConnectedUserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
