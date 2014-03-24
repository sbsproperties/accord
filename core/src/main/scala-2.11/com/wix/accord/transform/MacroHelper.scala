/*
  Copyright 2013-2014 Wix.com

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.wix.accord.transform

import MacroHelper._
trait MacroHelper[ C <: Context ] {
  /** The macro context (of type `C`), must be provided by the inheritor */
  protected val context: C

  import context.universe._

  def termName( symbol: String ): TermName = TermName( symbol )
  def resetAttrs( tree: Tree ): Tree = context.untypecheck( tree )
}

object MacroHelper {
  type Context = scala.reflect.macros.blackbox.Context
}
