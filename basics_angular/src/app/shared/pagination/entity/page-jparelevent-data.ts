export interface PageJPAReleventData<T> {
    
                
        content: T[
        ],
        pageable: {
            pageNumber:number,
            pageSize:number,
            sort: {
                empty: boolean,
                sorted: boolean,
                unsorted: boolean
            },
            offset: number,
            unpaged: boolean,
            paged: boolean
        },
        last: boolean,
        totalPages: number,
        totalElements: number,
        size: number,
        number: number,
        sort: {
            empty: boolean,
            sorted: boolean,
            unsorted: boolean
        },
        numberOfElements: number,
        first: boolean,
        empty: boolean
        
}
