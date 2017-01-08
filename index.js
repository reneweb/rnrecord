
import { NativeModules } from 'react-native';

const NativeRnRecord = NativeModules.RnRecord;

export default class RnRecord {
    id: Number;

    save(): Number {
        return NativeRnRecord.save(this.constructor.name, this._getProperties());
    }

    update(): Boolean {
        return NativeRnRecord.update(this.constructor.name, this._getProperties());
    }

    remove(): Boolean {
        return NativeRnRecord.remove(this.constructor.name, this._getProperties());
    }

    static findAll(): Array {
        return NativeRnRecord.findAll(this.name).map(rec => this._transformResultObj(this.name, rec));
    }

    static find(query: Object): Array {
        return NativeRnRecord.find(this.name, query).map(rec => this_.transformResultObj(this.name, rec));
    }

    _transformResultObj(className: String, obj: Object) {
        const inst = new className;
        Object.getOwnPropertyNames(obj).forEach(k => {
            inst[k] = obj[k];
        });

        return inst;
    }

    _getProperties(): Object {
        const propNames = Object.getOwnPropertyNames(this);
        return propNames.reduce( (obj, name) => {
          obj[name] = this[name]
          return obj;
        }, {});
    }
}
