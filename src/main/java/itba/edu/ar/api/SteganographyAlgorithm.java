package itba.edu.ar.api;

import itba.edu.ar.impl.LSBX;

public enum SteganographyAlgorithm {
    LSB1() {
        @Override
        public LSB getEncryptor() {
            return new LSBX(1);
        }
    }, LSB4() {
        @Override
        public LSB getEncryptor() {
            return new LSBX(4);
        }
    }, LSBI() {
        @Override
        public LSB getEncryptor() {
            return new LSBI();
        }
    };

    public abstract LSB getEncryptor();
}
