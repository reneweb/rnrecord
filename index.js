
import { NativeModules } from 'react-native';

const { RnRecord as NativeRnRecord } = NativeModules;

export default class RnRecord {
    id: Number;

    save(): Number {
        return NativeRnRecord.save(this.getProperties());
    }

    update(): Boolean {
        return NativeRnRecord.update(this.getProperties());
    }

    remove(): Boolean {
        return NativeRnRecord.remove(this.getProperties());
    }

    static findAll(): Array {
        return NativeRnRecord.findAll(this.name).map(rec => this.transformResultObj(this.name, rec));
    }

    static find(query: Object): Array {
        return NativeRnRecord.find(this.name, query).map(rec => this.transformResultObj(this.name, rec));
    }

    private transformResultObj(className: String, obj: Object) {
        const inst = new className;
        Object.getOwnPropertyNames(obj).forEach(k => {
            inst[k] = obj[k];
        });

        return inst;
    }

    private getProperties() {
        const propNames = Object.getOwnPropertyNames(this);
        return propNames.reduce( (obj, name) => obj[name] = this[name], {});
    }
}
