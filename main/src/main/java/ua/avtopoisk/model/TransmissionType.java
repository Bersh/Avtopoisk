package ua.avtopoisk.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents transmission type
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 09.10.12
 */
public enum TransmissionType implements Parcelable {
    NA, AUTO, MECHANIC;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(ordinal());
    }

    public static final Creator<TransmissionType> CREATOR = new Creator<TransmissionType>() {
        @Override
        public TransmissionType createFromParcel(final Parcel source) {
            return TransmissionType.values()[source.readInt()];
        }

        @Override
        public TransmissionType[] newArray(final int size) {
            return new TransmissionType[size];
        }
    };
}
