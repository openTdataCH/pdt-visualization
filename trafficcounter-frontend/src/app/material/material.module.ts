import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";

const MATERIAL_MODULES = [
  MatToolbarModule,
  MatButtonModule,
  MatIconModule
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    ...MATERIAL_MODULES
  ],
  exports: MATERIAL_MODULES
})
export class MaterialModule { }
