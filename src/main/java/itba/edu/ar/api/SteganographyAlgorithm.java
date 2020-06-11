package itba.edu.ar.api;

import itba.edu.ar.impl.Mirror;

public enum SteganographyAlgorithm {
    LSB1() {
        @Override
        public LSB getEncryptor() {
            return null;
        }
    }, LSB4() {
        @Override
        public LSB getEncryptor() {
            return null;
        }
    }, LSBI() {
        @Override
        public LSB getEncryptor() {
            return null;
        }
    }, MIRROR() {
        @Override
        public LSB getEncryptor() {
            return new Mirror();
        }
    };

    public abstract LSB getEncryptor();
}
