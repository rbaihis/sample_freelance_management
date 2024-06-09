import { TestBed } from '@angular/core/testing';

import { FileDocService } from './file-doc.service';

describe('FileDocService', () => {
  let service: FileDocService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FileDocService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
