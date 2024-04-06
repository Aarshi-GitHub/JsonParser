import { Component } from '@angular/core';
import { RestClientServiceService } from '../rest-client-service.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-text-tab',
  templateUrl: './text-tab.component.html',
  styleUrls: ['./text-tab.component.scss']
})
export class TextTabComponent {
  parserForm!:FormGroup;
  jsonData: string = '';
  beautifiedJson: string = '';
  invalidJson=false;
  parsedSuccesfully=false;

  constructor(private  dataService:RestClientServiceService,
    private fb:FormBuilder){}

  ngOnInit()
  {
    this.initParserForm();
  }

  initParserForm()
  {
    this.parserForm=this.fb.group({
      jsonData:['',[Validators.required]]
    });
   
  }
  
  checkValidity()
  {
      try {
          JSON.parse(this.parserForm.get('jsonData')?.value);
          return true; 
      } catch (error) {
        this.parsedSuccesfully=false;
          return false; 
      }
  }
  

  beautifyJson() {
    const jsonData = this.parserForm.get('jsonData')?.value;
    try {
      const parsedJson = JSON.parse(jsonData);
      this.beautifiedJson = JSON.stringify(parsedJson, null, 2);
      this.parserForm.patchValue({ jsonData: this.beautifiedJson });
    } catch (error) {
      console.error('Invalid JSON format:', error);
      this.parsedSuccesfully=false;
      this.parserForm.controls['jsonData'].setErrors({invalid:true});
    }
  }

  jsonOnSubmit(): void {
    if(this.checkValidity())
    {
      let request=this.parserForm.controls['jsonData'].value;
      this.parsedSuccesfully=true;
      this.dataService.getData(request).subscribe(
        (response)=>{
          this.parsedSuccesfully=true;

      },(error:HttpErrorResponse)=>{
          console.error(error,'text area failed');
      })
    }else{
      this.parsedSuccesfully=false;
      this.parserForm.controls['jsonData'].setErrors({invalid:true});
    }
}
}
