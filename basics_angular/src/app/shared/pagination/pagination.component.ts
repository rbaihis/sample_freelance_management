import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css'
})
export class PaginationComponent {
  @Input() totalPages: number=0;
  @Input() currentPage: number=0;
  @Output() pageChange:EventEmitter<number> = new EventEmitter<number>();

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i );
  }

   previousPage(): void {
    if (this.currentPage >= 1) {
      this.pageChange.emit(this.currentPage - 1);
    }
  }

  nextPage(): void {
    if (this.currentPage+1 < this.totalPages) {
      this.pageChange.emit(this.currentPage +1);
    }
    
  }

 
  gotoPage(page: number): void {
    this.pageChange.emit(page);
  }

}


