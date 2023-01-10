/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {Component, OnInit} from '@angular/core';
import {LanguageService} from "../../services/language/language.service";

/**
 * Header of the application.
 */
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(readonly languageService: LanguageService) {
  }

  ngOnInit(): void {
  }

  /**
   * Sets the language.
   * @param language language to set
   */
  setLanguage(language: string) {
    this.languageService.currentLanguage$.next(language);
  }

}
