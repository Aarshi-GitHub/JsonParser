import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RestClientServiceService {

  constructor(private http:HttpClient) { }

  public getData(data:any):Observable<any>{
    return this.http.post<any>('http://localhost:8080/api/v1/upload', data);
  }
}
