import uuid
from locust import HttpUser, task, TaskSet, between


class FinnHubApiGetShares(TaskSet):
    user_id = "4028b8817a118a78017a118a7b7c0000"

    @task
    def get_shares(self):
        self.client.get("/api/users/{}/shares".format(self.user_id), name="get shares")


class FinnHubApiBuyShare(TaskSet):
    user_id = "4028b8817a118a78017a118a7b7c0000"

    @task
    def buy_share(self):
        self.client.post(url="/api/users/shares",
                         json={"userId": self.user_id, "symbol": "TSLA", "quantity": "2"},
                         headers={"Content-Type": "application/json"}, name="buy share")


class FinnHubApiGetUserShareInfoByStockSymbol(TaskSet):
    user_id = "4028b8817a118a78017a118a7b7c0000"
    stock_symbol = "TSLA"

    @task
    def get_share_info_by_stock_symbol(self):
        self.client.get("/api/users/{}/shares/{}".format(self.user_id, self.stock_symbol), name="get shares")


class CreateUser(TaskSet):
    @task
    def create_new_user(self):
        self.client.post(url="/api/users",
                         json={"firstName": "RandomName" + str(uuid.uuid4()), "lastName": "MyLastName"},
                         headers={"Content-Type": "application/json"}, name="create new user")


class UserTaskSet(TaskSet):
    tasks = {CreateUser: 1, FinnHubApiGetShares: 5}


class WebUser(HttpUser):
    tasks = [UserTaskSet]
    wait_time = between(2, 3)


class FinnHubWebUser(HttpUser):
    tasks = [FinnHubApiBuyShare]
    wait_time = between(30, 32)
