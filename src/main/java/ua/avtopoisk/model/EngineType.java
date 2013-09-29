package ua.avtopoisk.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents engine type
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 09.10.12
 */
public enum EngineType implements Parcelable {
    NA, DISEL, GAS;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(ordinal());
    }

    public static final Creator<EngineType> CREATOR = new Creator<EngineType>() {
        @Override
        public EngineType createFromParcel(final Parcel source) {
            return EngineType.values()[source.readInt()];
        }

        @Override
        public EngineType[] newArray(final int size) {
            return new EngineType[size];
        }
    };
}
