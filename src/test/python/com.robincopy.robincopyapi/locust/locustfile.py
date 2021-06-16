import uuid
from locust import HttpUser, task, TaskSet, between, SequentialTaskSet

user_id = "2c9280827a15dd51017a15dd56800000"

class FinnHubApiGetShares(TaskSet):
    @task
    def get_shares(self):
        self.client.get("/api/users/{}/shares".format(user_id),
                        headers={"Accept-Encoding": "*", "Connection": "keep-alive"},
                        name="get shares")


class FinnHubApiBuyShare(TaskSet):
    @task
    def buy_share(self):
        self.client.post(url="/api/users/shares",
                         json={"userId": user_id, "symbol": "TSLA", "quantity": "2"},
                         headers={"Content-Type": "application/json"}, name="buy share")


class FinnHubApiGetUserShareInfoByStockSymbol(TaskSet):
    stock_symbol = "TSLA"

    @task
    def get_share_info_by_stock_symbol(self):
        self.client.get("/api/users/{}/shares/{}".format(user_id, self.stock_symbol),
                        name="get share info by stock symbol")


class FinnHubApiGetUserSharesSummary(TaskSet):
    stock_symbol = "TSLA"

    @task
    def get_user_shares_summary(self):
        self.client.get("/api/users/{}/shares/{}".format(user_id, self.stock_symbol),
                        name="get user shares summary")


class CreateUser(TaskSet):
    @task
    def create_new_user(self):
        self.client.post(url="/api/users",
                         json={"firstName": "RandomName" + str(uuid.uuid4()), "lastName": "MyLastName"},
                         headers={"Content-Type": "application/json"}, name="create new user")


class RobinCopyTaskSet(TaskSet):
    tasks = {CreateUser: 1, FinnHubApiGetShares: 30, FinnHubApiBuyShare: 40,
             FinnHubApiGetUserShareInfoByStockSymbol: 50, FinnHubApiGetUserSharesSummary: 30}


class MockedFinnHubWebUser(HttpUser):
    tasks = [RobinCopyTaskSet]
    wait_time = between(3, 5)


class RealFinnHubWebUser(HttpUser):
    tasks = [FinnHubApiGetShares]
    wait_time = between(30, 32)
