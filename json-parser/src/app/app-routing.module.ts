import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TextTabComponent } from './text-tab/text-tab.component';

const routes: Routes = [
  { path: '', component: TextTabComponent }, 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
  
 }
