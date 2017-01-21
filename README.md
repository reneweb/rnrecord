
# RnRecord

RnRecord is a active record library for react native.

## Getting started

`$ npm install rnrecord --save`

### Installation

`$ react-native link rnrecord`

For iOS it is additionally required to link libsqlite.
In order to do that, open the project in xcode,
click on the project in the project navigator,
go to build phases -> link binary with libraries,
click the + symbol, search for libsqlite3.tbd and choose that library.

### Usage

```javascript
import RnRecord from 'rnrecord';

class Person extends RnRecord {
  name: String;
  age: Number;

  constructor(name: String, age: Number) {
    super();
    this.name = name;
    this.age = age;
  }
}

const p = new Person("rob", 29);
p.save().then(id => {
  p.age = 30;
  p.update().then(res => {
    Person.find({age: 30}).then(r => console.log(r));
  });
});
