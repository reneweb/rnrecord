
import { NativeModules } from 'react-native';

const NativeRnRecord = NativeModules.RnRecord;

export default class RnRecord {
    id: Number;

    save(): Promise<Number> {
        return NativeRnRecord.save(this.constructor.name, this._getProperties()).then(id => {
          this.id = id;
          return id;
        });
    }

    update(): Promise<Boolean> {
        return NativeRnRecord.update(this.constructor.name, this._getProperties()).then(res => res != -1);
    }

    remove(): Promise<Boolean> {
        return NativeRnRecord.remove(this.constructor.name, this._getProperties()).then(res => res != -1);
    }

    static findAll(): Promise<Array> {
        return NativeRnRecord.findAll(this.name).then(records => records.map(rec => this._transformResultObj(this.name, rec)));
    }

    static find(query: Object): Promise<Array> {
        return NativeRnRecord.find(this.name, query).then(records => records.map(rec => this._transformResultObj(this.name, rec)));
    }

    static _transformResultObj(className: String, obj: Object) {
        const inst = new this();
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
