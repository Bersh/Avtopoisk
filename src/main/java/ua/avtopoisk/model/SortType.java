package ua.avtopoisk.model;

/**
 * Represents sorting type
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 09.10.12
 */
public enum SortType {
    /**
     * Don't change items order! It's uses for select current sort type in {@link ua.avtopoisk.activites.SearchResultActivity#onCreateOptionsMenu(android.view.Menu)}
     */
    DATE {
        public int getAvtopoiskCode() {
            return 0;
        }
    },
    YEAR_DESC {
        public int getAvtopoiskCode() {
            return 1;
        }
    },
    YEAR_INC {
        public int getAvtopoiskCode() {
            return 3;
        }
    },
    PRICE_DESC {
        public int getAvtopoiskCode() {
            return 4;
        }
    },
    PRICE_INC {
        public int getAvtopoiskCode() {
            return 2;
        }
    };

    public abstract int getAvtopoiskCode();
}
