.PHONY: all
all: create_db create_test_db

create_db:
	@echo "Creating DB: reviews_next.db"
	@sqlite3 reveiws_next.db "VACUUM" > /dev/null

create_test_db:
	@echo "Creating test DB: test.reviews_next.db"
	@sqlite3 test.reviews_next.db "VACUUM" > /dev/null

HOST = "local.reviews-next"
HOST_PRESENT = $(shell grep $(HOST) /etc/hosts)
add_host:
ifeq ($(strip $(HOST_PRESENT)), )
	@echo "Addding local.reviews-next to /etc/hosts"
	@echo "127.0.0.1	$(HOST)" >> /etc/hosts
else
	@echo "Host $(HOST) already present"
endif
