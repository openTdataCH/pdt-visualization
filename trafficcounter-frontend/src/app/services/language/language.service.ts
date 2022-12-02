import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {TranslateService} from "@ngx-translate/core";

const KEY_LANGUAGE = 'lang';

/**
 * Service for language handling.
 */
@Injectable({
  providedIn: 'root'
})
export class LanguageService {

  /**
   * The default language.
   */
  static readonly defaultLanguage = 'en';

  /**
   * All available languages.
   */
  readonly languages = [
    'de',
    'en'
  ];

  /**
   * The currently applied language.
   */
  currentLanguage$: BehaviorSubject<string> = new BehaviorSubject<string>(LanguageService.defaultLanguage);

  constructor(private readonly translateService: TranslateService) {
    this.currentLanguage$.next(localStorage.getItem(KEY_LANGUAGE) ?? LanguageService.defaultLanguage);
    this.currentLanguage$.subscribe(language => {
      this.translateService.use(language);
      localStorage.setItem(KEY_LANGUAGE, language);
    });
  }

}
