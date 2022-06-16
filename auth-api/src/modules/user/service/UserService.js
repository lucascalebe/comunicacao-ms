import UserRepository from "../repository/UserRepository"
import * as httpStatus from "../../../config/constants/httpStatus";

class UserService {

    async findByEmail(req) {
        try {
            const { email } = req.params;
            this.validateRequestData(email);
            let user = UserRepository.findByEmail(email)
            if (!user) {

            }
            return {
                status: httpStatus.SUCCESS,
                user: {
                    id: user.id,
                    name: user.name,
                    email: user.email
                }
            }
        } catch (err) {
            return {
                status: err.status ? err.status : httpStatus.INTERAL_SERVER_ERROR,
                message: err.status
            }
        }
    }

    validateRequestData(email) {
        if (!email) {
            throw new Error('Uer email was not informed.')
        }
    }
}

export default new UserService();