(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
  typeof define === 'function' && define.amd ? define(['exports'], factory) :
  (global = typeof globalThis !== 'undefined' ? globalThis : global || self, factory(global.fooLib = {}));
})(this, (function (exports) { 'use strict';

  function say() {
    console.log("this is foo");
  }

  exports.say = say;

}));
