/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {HttpUrlEncodingCodec} from '@angular/common/http';

/**
 * CustomHttpUrlEncodingCodec
 * Fix plus sign (+) not encoding, so sent as blank space
 * See: https://github.com/angular/angular/issues/11058#issuecomment-247367318
 */
export class CustomHttpUrlEncodingCodec extends HttpUrlEncodingCodec {
  override encodeKey(k: string): string {
    k = super.encodeKey(k);
    return k.replace(/\+/gi, '%2B');
  }

  override encodeValue(v: string): string {
    v = super.encodeValue(v);
    return v.replace(/\+/gi, '%2B');
  }
}

