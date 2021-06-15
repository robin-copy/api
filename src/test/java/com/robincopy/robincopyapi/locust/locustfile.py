import uuid
from locust import HttpUser, task, between


class RobinCopyTest(HttpUser):
    wait_time = between(12, 13)

    #@task
    #def getShares(self):
    #    self.client.get("/api/users/4028b8817a085189017a08518f910000/shares")

    #@task
    #def addNewShare(self):
    #    self.client.post(url="/api/users/shares",
    #                   json={"userId": "2c9ac0817a0d751d017a0d7521950000", "symbol": "TSLA", "quantity": "2"},
    #                   headers={"Content-Type": "application/json"})

    @task
    def newUser(self):
        self.client.post(url="/api/users",
                         json={"firstName": "random " + str(uuid.uuid4()), "lastName": "Papaa"},
                         headers={"Content-Type": "application/json"})
