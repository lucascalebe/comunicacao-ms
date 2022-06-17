import bcrypt from "bcrypt";
import User from "../../modules/user/model/User";

export async function createInitalData() {
    try {
        await User.sync({ force: true })
    
        let password = await bcrypt.hash('123456', 10);
    
        await User.create({
            name: 'User test 1',
            email: 'testuser1@gmail.com',
            password: password
        });

        await User.create({
            name: 'User test 2',
            email: 'testuser2@gmail.com',
            password: password
        });
    } catch (err) {
        console.log(err)
    }
}